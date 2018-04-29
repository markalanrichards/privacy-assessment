import express from 'express'
import clientFactory from '../../src/client.factory'

const sF = (prefix, protocol, clientBuilder, handlers) => {
  const app = express()
  const protocolName = protocol.getName()
  const path = `${prefix}/${protocolName}`
  handlers.forEach(handler =>
      protocol.on(handler.method, (req, ee, cb) =>
      cb(null, handler.handle(req))
    )
  )
  app.post(path, (req, res) =>
      protocol.createListener(cb => {
      cb(res)
      return req
    })
  )
  let server
  const promiseServerAndClient = new Promise(resolvePort => {
    server = app.listen(0, () => {
      resolvePort(server.address().port)
    })
  }).then(port => {
    return {
      client: clientBuilder(clientFactory('127.0.0.1', prefix, port, "http:")),
      server
    }
  })
  return promiseServerAndClient
}

export default sF
