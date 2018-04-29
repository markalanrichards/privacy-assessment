const fs = require('fs');
const avprFolder = 'avpr/';
const files = fs.readdirSync(avprFolder);
files.forEach(file => {
  const data = fs.readFileSync(avprFolder + file, {
    encoding: 'utf8'
  });
  fs.writeFileSync(
    'src/' + avprFolder + file + '.js',
    'export default ' + data,
    { encoding: 'utf8' }
  );
});
