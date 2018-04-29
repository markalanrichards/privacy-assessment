import * as http from 'http';

const cpF = (host, prefix, port, protocol) => clientProtocol => {

  const path = `${prefix}/${clientProtocol.getName()}`;

  const ee = clientProtocol.createEmitter(cb =>
    http
      .request({
        headers: { 'content-type': 'avro/binary' },
        method: 'POST',
        host,
        port,
        path
      })
      .on('response', res => cb(res))
  );
  return (method: string, parameters: any): Promise<any> => {
    console.log({ method, parameters: JSON.stringify(parameters) });
    return new Promise((resolve, reject) =>
      clientProtocol.emit(method, parameters, ee, (err: any, res: any) => {

        console.log({ err:JSON.stringify(err), res:JSON.stringify(res) });
        err ? reject(err) : resolve(res);
      })
    );
  };
};

export default cpF;
