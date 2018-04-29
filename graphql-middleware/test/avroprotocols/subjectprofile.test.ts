import { Server } from "http";

declare var describe: any;
declare var it: any;
declare var expect: any;
declare var beforeEach: any;
declare var afterEach: any;
import {
  protocolFactory,
  SubjectProfileAvro,
  subjectProfileClientFactory,
  SubjectProfileCreateAvro,
  SubjectProfileUpdateAvro
} from "../../src/avroprotocols/subjectProfileProtocol";
import { randomText } from "../util/random";
import serverFactory from "../util/server.factory";

describe("SubjectProfileAvsc", () => {
  let randomSubjectProfile: SubjectProfileAvro;
  let randomCreateSubjectProfile: SubjectProfileCreateAvro;
  let randomUpdateSubjectProfile: SubjectProfileUpdateAvro;
  let client: any;
  let server: Server;
  function randomiseCreateSubjectProfile(): SubjectProfileCreateAvro {
    return {
      customerProfileId: randomText(),
      externalSubjectName: randomText(),
      externalSubjectReference: randomText()
    } as SubjectProfileCreateAvro;
  }

  function randomiseSubjectProfile(): SubjectProfileAvro {
    return {
      epoch: randomText(),
      customerProfileId: randomText(),
      externalSubjectName: randomText(),
      externalSubjectReference: randomText(),
      id: randomText(),
      version: randomText()
    };
  }
  function randomiseUpdateSubjectProfile(): SubjectProfileUpdateAvro {
    return {
      customerProfileId: randomText(),
      externalSubjectName: randomText(),
      externalSubjectReference: randomText(),
      id: randomText(),
      lastVersion: randomText()
    };
  }

  beforeEach(() => {
    randomSubjectProfile = randomiseSubjectProfile();
    randomCreateSubjectProfile = randomiseCreateSubjectProfile();
    randomUpdateSubjectProfile = randomiseUpdateSubjectProfile();

    return serverFactory("", protocolFactory(), subjectProfileClientFactory, [
      {
        method: "avroCreateSubjectProfile",
        handle: req => {
          expect(req.request).toEqual(randomCreateSubjectProfile);
          return randomSubjectProfile;
        }
      },
      {
        method: "avroUpdateSubjectProfile",
        handle: req => {
          expect(req.update).toEqual(randomUpdateSubjectProfile);
          return randomSubjectProfile;
        }
      },
      {
        method: "avroReadSubjectProfile",
        handle: req => {
          expect(req).toEqual({
            id: randomSubjectProfile.id
          });
          return randomSubjectProfile;
        }
      },
      {
        method: "avroReadVersionedSubjectProfile",
        handle: req => {
          expect(req).toEqual({
            id: randomSubjectProfile.id,
            version: randomSubjectProfile.version
          });
          return randomSubjectProfile;
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
      .avroCreateSubjectProfile(randomCreateSubjectProfile)
      .then(subjectProfile =>
        expect(subjectProfile).toEqual(randomSubjectProfile)
      ));
  it("update", () =>
    client
      .avroUpdateSubjectProfile(randomUpdateSubjectProfile)
      .then(subjectProfile =>
        expect(subjectProfile).toEqual(randomSubjectProfile)
      ));
  it("read", () =>
    client
      .avroReadVersionedSubjectProfile(
        randomSubjectProfile.id,
        randomSubjectProfile.version
      )
      .then(subjectProfile =>
        expect(subjectProfile).toEqual(randomSubjectProfile)
      ));
  it("readVersion", () =>
    client
      .avroReadSubjectProfile(randomSubjectProfile.id)
      .then(subjectProfile =>
        expect(subjectProfile).toEqual(randomSubjectProfile)
      ));
});
