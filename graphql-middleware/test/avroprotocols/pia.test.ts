import { Server } from "http";

declare var describe: any;
declare var it: any;
declare var expect: any;
declare var beforeEach: any;
declare var afterEach: any;

import {
  PIAAnnex1,
  PIAAnnex2,
  PIAAnnex2Step3,
  PIAAnnex2Step4,
  PIAAnnex2Step5,
  PIAAnnex2Step6,
  PIAAnnex3,
  PIAAnnex3Principle1,
  PIAAnnex3Principle2,
  PIAAnnex3Principle3,
  PIAAnnex3Principle4,
  PIAAnnex3Principle5,
  PIAAnnex3Principle6,
  PIAAnnex3Principle7,
  PIAAnnex3Principle8,
  PIAAvro,
  piaClientFactory,
  PIACreateAvro,
  PIADocument,
  PIAUpdateAvro,
  protocolFactory
} from "../../src/avroprotocols/piaProtocol";
import { randomText } from "../util/random";
import serverFactory from "../util/server.factory";

describe("SubjectProfileAvsc", () => {
  let randomPIA: PIAAvro;
  let randomCreatePIA: PIACreateAvro;
  let randomUpdatePIA: PIAUpdateAvro;
  let client: any;
  let server: Server;
  function randomAnnex1(): PIAAnnex1 {
    return {
      question1: randomText(),
      question2: randomText(),
      question3: randomText(),
      question4: randomText(),
      question5: randomText(),
      question6: randomText(),
      question7: randomText()
    };
  }
  function randomAnnex2Step3(): PIAAnnex2Step3 {
    return {
      field1: randomText(),
      field2: randomText(),
      field3: randomText(),
      field4: randomText()
    };
  }
  function randomAnnex2Step4(): PIAAnnex2Step4 {
    return {
      field1: randomText(),
      field2: randomText(),
      field3: randomText(),
      field4: randomText()
    };
  }
  function randomAnnex2Step5(): PIAAnnex2Step5 {
    return {
      field1: randomText(),
      field2: randomText(),
      field3: randomText()
    };
  }
  function randomAnnex2Step6(): PIAAnnex2Step6 {
    return {
      field1: randomText(),
      field2: randomText(),
      field3: randomText()
    };
  }
  function randomAnnex2(): PIAAnnex2 {
    return {
      step1: randomText(),
      step2Part1: randomText(),
      step2Part2: randomText(),
      step3: randomAnnex2Step3(),
      step4: randomAnnex2Step4(),
      step5: randomAnnex2Step5(),
      step6: randomAnnex2Step6()
    };
  }
  function randomAnnex3Principle1(): PIAAnnex3Principle1 {
    return {
      question1: randomText(),
      question2: randomText(),
      question3: randomText(),
      question4: randomText(),
      question5: randomText(),
      question6: randomText(),
      question7: randomText(),
      question8: randomText(),
      question9: randomText()
    };
  }
  function randomAnnex3Principle2(): PIAAnnex3Principle2 {
    return {
      question1: randomText(),
      question2: randomText()
    };
  }
  function randomAnnex3Principle3(): PIAAnnex3Principle3 {
    return {
      question1: randomText(),
      question2: randomText()
    };
  }
  function randomAnnex3Principle4(): PIAAnnex3Principle4 {
    return {
      question1: randomText(),
      question2: randomText()
    };
  }
  function randomAnnex3Principle5(): PIAAnnex3Principle5 {
    return {
      question1: randomText(),
      question2: randomText()
    };
  }
  function randomAnnex3Principle6(): PIAAnnex3Principle6 {
    return {
      question1: randomText(),
      question2: randomText()
    };
  }
  function randomAnnex3Principle7(): PIAAnnex3Principle7 {
    return {
      question1: randomText(),
      question2: randomText()
    };
  }
  function randomAnnex3Principle8(): PIAAnnex3Principle8 {
    return {
      question1: randomText(),
      question2: randomText()
    };
  }
  function randomAnnex3(): PIAAnnex3 {
    return {
      principle1: randomAnnex3Principle1(),
      principle2: randomAnnex3Principle2(),
      principle3: randomAnnex3Principle3(),
      principle4: randomAnnex3Principle4(),
      principle5: randomAnnex3Principle5(),
      principle6: randomAnnex3Principle6(),
      principle7: randomAnnex3Principle7(),
      principle8: randomAnnex3Principle8()
    };
  }
  function randomDocument(): PIADocument {
    return {
      annex1: randomAnnex1(),
      annex2: randomAnnex2(),
      annex3: randomAnnex3()
    };
  }
  function randomiseCreateSubjectProfile(): PIACreateAvro {
    return {
      subjectProfileId: randomText(),
      document: randomDocument()
    };
  }

  function randomiseSubjectProfile(): PIAAvro {
    return {
      epoch: randomText(),
      subjectProfileId: randomText(),
      document: randomDocument(),
      id: randomText(),
      version: randomText()
    };
  }
  function randomiseUpdateSubjectProfile(): PIAUpdateAvro {
    return {
      subjectProfileId: randomText(),
      document: randomDocument(),
      id: randomText(),
      lastVersion: randomText()
    };
  }

  beforeEach(() => {
    randomPIA = randomiseSubjectProfile();
    randomCreatePIA = randomiseCreateSubjectProfile();
    randomUpdatePIA = randomiseUpdateSubjectProfile();

    return serverFactory("", protocolFactory(), piaClientFactory, [
      {
        method: "avroCreatePIA",
        handle: req => {
          expect(req.request).toEqual(randomCreatePIA);
          return randomPIA;
        }
      },
      {
        method: "avroUpdatePIA",
        handle: req => {
          expect(req.update).toEqual(randomUpdatePIA);
          return randomPIA;
        }
      },
      {
        method: "avroReadPIA",
        handle: req => {
          expect(req).toEqual({
            id: randomPIA.id
          });
          return randomPIA;
        }
      },
      {
        method: "avroReadVersionedPIA",
        handle: req => {
          expect(req).toEqual({
            id: randomPIA.id,
            version: randomPIA.version
          });
          return randomPIA;
        }
      }
    ]).then(serverAndClient => {
      server = serverAndClient.server;
      client = serverAndClient.client;
    });
  });
  afterEach(() => new Promise(resolve => server.close(resolve)));
  it("create", () =>
    client
      .avroCreatePIA(randomCreatePIA)
      .then(pia => expect(pia).toEqual(randomPIA)));
  it("update", () =>
    client
      .avroUpdatePIA(randomUpdatePIA)
      .then(pia => expect(pia).toEqual(randomPIA)));
  it("read", () =>
    client
      .avroReadVersionedPIA(randomPIA.id, randomPIA.version)
      .then(pia => expect(pia).toEqual(randomPIA)));
  it("readVersion", () =>
    client
      .avroReadPIA(randomPIA.id)
      .then(pia => expect(pia).toEqual(randomPIA)));
});
