const fs = require('fs');
const yaml = require('js-yaml');
const exec = require('child_process').exec;

const concourseCredentials  = process.env.CONCOURSE_CREDENTIALS
const writeCredentialsTofIle = () => {
    const credentialsLocation = "credentials.yml"
    fs.writeFile(credentialsLocation,concourseCredentials,()=>{})
    return credentialsLocation
}

const concourseCredentialsFile = concourseCredentials ? writeCredentialsTofIle() : process.env.CONCOURSE_CREDENTIALS_FILE


const promiseVarsByUuid = new Promise(resVarsByUuid => {
    fs.readFile("vars_by_uuid.yml","utf8", (err,file)=> {
        resVarsByUuid(yaml.load(file))
    })
})
const promiseTasks = new Promise((resTasks, rej) => {
    var tasks = {}
    fs.readdir("./tasks", (err, files) => {
        const promisedFiles = files.map(filename => {
            return new Promise((resFile, rejFile) => {
                fs.readFile(`tasks/${filename}`, 'utf8', (err, file) => {
                    let message = yaml.load(file);
                    tasks[message.task] = message
                    resFile();
                })
            });
        });
        Promise.all(promisedFiles).then(() => {
            resTasks(tasks);
        })
    })
})
const pipeline = {}
const promiseResources = new Promise((resResources, rej) => {
    var resources = []
    fs.readdir("./resources", (err, files) => {
        const promisedFiles = files.map(filename => {
            return new Promise((resFile, rejFile) => {
                fs.readFile(`resources/${filename}`, 'utf8', (err, file) => {
                    let message = yaml.load(file,"DEFAULT_FULL_SCHEMA");
                    message.check_every = "5m"
                    resources.push(message)
                    resFile();
                })
            });
        });
        Promise.all(promisedFiles).then(() => {
            pipeline.resources = resources
            resResources(resources);
        })
    })
})
const promiseResourceTypes = new Promise((resResourcesTypes, rej) => {
    var resourceTypes = []
    fs.readdir("./resource_types", (err, files) => {
        const promisedFiles = files.map(filename => {
            return new Promise((resFile, rejFile) => {
                fs.readFile(`resource_types/${filename}`, 'utf8', (err, file) => {
                    let message = yaml.load(file);
                    resourceTypes.push(message)
                    resFile();
                })
            });
        });
        Promise.all(promisedFiles).then(() => {
            pipeline.resource_types = resourceTypes
            resResourcesTypes(resourceTypes);
        })
    })
})
const promiseJobs = new Promise((resJobs, rej) => {
    var jobs = []
    fs.readdir("./jobs", (err, files) => {
        const promisedFiles = files.map(filename => {
            return new Promise((resFile, rejFile) => {
                fs.readFile(`jobs/${filename}`, 'utf8', (err, file) => {
                    let message = yaml.load(file);
                    jobs.push(message)
                    resFile();
                })
            });
        });
        Promise.all(promisedFiles).then(() => {
            resJobs(jobs);
        })
    })
}).then(jobs => {

    return promiseTasks.then(tasks => {
        pipeline.jobs = jobs.map(job => {
            job.plan = job.plan.map(step => {
                if (undefined != step.task) {
                    return tasks[step.task]
                }
                return step
            })
            return job
        })

    })
})
Promise.all([promiseJobs, promiseResources, promiseResourceTypes]).then(()=> new Promise((resExec, rejExec) => {
    promiseVarsByUuid.then(varsByUuid=> {
        let message = yaml.safeDump(pipeline,"DEFAULT_FULL_SCHEMA");

        Object.keys(varsByUuid).map(key => {
            message = message.replace(new RegExp(key, "g"),`((${varsByUuid[key]}))`)
        })

        fs.writeFile("pipeline.yml",message,()=>{})
        resExec();
    })
})).then(() => new Promise((resFile, rejFile) => {

    fs.readFile(concourseCredentialsFile, 'utf8', (err, file) => {
        let message = yaml.load(file);

        if(message.hasOwnProperty('concourseCredentials')) {
          delete message.concourseCredentials;
        }

        message.concourseCredentials = yaml.safeDump(Object.assign({}, message),"DEFAULT_FULL_SCHEMA");
        if(message.kubeconfig) {
          console.log(JSON.stringify(message))
          fs.writeFile(concourseCredentialsFile,yaml.safeDump(message),()=>{})
          resFile();
        }
        else {
          exec("kubectl config view --minify=true --flatten", (error,stdout,stderr) => {
              message.kubeconfig = stdout
              console.log(JSON.stringify(message))
              fs.writeFile(concourseCredentialsFile,yaml.safeDump(message),()=>{})
              resFile();
            })
        }

    })



})).then(()=>new Promise((resExec, rejExec)=>{
  console.log(`fly -t ci login -c ${process.env.CONCOURSE_LOGIN_URL} -u concourse -p concourse`)
  exec(`fly -t ci login -c ${process.env.CONCOURSE_LOGIN_URL} -u concourse -p concourse`,(error, stdout,stderr)=>
  {
    console.log(stdout)
    console.log(stderr)

    exec(`fly -t ci set-pipeline -n -p privacy-assessment -c pipeline.yml --load-vars-from=${concourseCredentialsFile}`,(error, stdout,stderr)=>
    {
      console.log(stdout)
      console.log(stderr)
      exec(`fly -t ci unpause-pipeline -p privacy-assessment`,(error, stdout,stderr)=>
      {
        console.log(stdout)
        console.log(stderr)

        resExec()
      })
    })
  })
}))
