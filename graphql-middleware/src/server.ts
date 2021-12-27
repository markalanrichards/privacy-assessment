import * as express from "express";
import * as cors from "cors";

export const serverFactory = (port: string) => {
  const server = express();

  server.use(cors());

  server.use(express.json());

  server.post("/mutation", function(req, res) {
    console.log(req.body);
    res.send("POST request rercived to /mutation");
  });

  const listener = server.listen(port, () => {
    const address = listener.address();
    // tslint:disable-next-line
    console.log(`Listening on http://127.0.0.1:${address.port}`);
  });
};
