import { Server } from "http";

declare var describe: any;
declare var it: any;
declare var expect: any;
declare var beforeEach: any;
declare var afterEach: any;
import {
  CustomerProfileAvro,
  customerProfileClientFactory,
  CustomerProfileCreateAvro,
  CustomerProfileUpdateAvro,
  protocolFactory
} from "../../src/avroprotocols/customerProfileProtocol";
import { randomInt, randomText } from "../util/random";
import serverFactory from "../util/server.factory";

describe("CustomerProfileAvsc", () => {
  let randomCustomerProfile: CustomerProfileAvro;
  let randomCreateCustomerProfile: CustomerProfileCreateAvro;
  let randomUpdateCustomerProfile: CustomerProfileUpdateAvro;
  let client: any;
  let server: Server;

  function randomiseCreateCustomerProfile(): CustomerProfileCreateAvro {
    return {
      externalEmail: randomText(),
      externalLegalName: randomText()
    };
  }

  function randomiseCustomerProfile(): CustomerProfileAvro {
    return {
      epoch: randomText(),
      externalEmail: randomText(),
      externalLegalName: randomText(),
      id: randomText(),
      version: randomText()
    };
  }
  function randomiseUpdateCustomerProfile(): CustomerProfileUpdateAvro {
    return {
      externalEmail: randomText(),
      externalLegalName: randomText(),
      id: randomText(),
      lastVersion: randomText()
    };
  }

  beforeEach(() => {
    randomCustomerProfile = randomiseCustomerProfile();
    randomCreateCustomerProfile = randomiseCreateCustomerProfile();
    randomUpdateCustomerProfile = randomiseUpdateCustomerProfile();

    return serverFactory("", protocolFactory(), customerProfileClientFactory, [
      {
        method: "avroCreateCustomerProfile",
        handle: req => {
          expect(req.request).toEqual(randomCreateCustomerProfile);
          return randomCustomerProfile;
        }
      },
      {
        method: "avroUpdateCustomerProfile",
        handle: req => {
          expect(req.update).toEqual(randomUpdateCustomerProfile);
          return randomCustomerProfile;
        }
      },
      {
        method: "avroReadCustomerProfile",
        handle: req => {
          expect(req).toEqual({
            id: randomCustomerProfile.id
          });
          return randomCustomerProfile;
        }
      },
      {
        method: "avroReadVersionedCustomerProfile",
        handle: req => {
          expect(req).toEqual({
            id: randomCustomerProfile.id,
            version: randomCustomerProfile.version
          });
          return randomCustomerProfile;
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
      .avroCreateCustomerProfile(randomCreateCustomerProfile)
      .then(customerProfile =>
        expect(customerProfile).toEqual(randomCustomerProfile)
      ));
  it("update", () =>
    client
      .avroUpdateCustomerProfile(randomUpdateCustomerProfile)
      .then(customerProfile =>
        expect(customerProfile).toEqual(randomCustomerProfile)
      ));
  it("read", () =>
    client
      .avroReadVersionedCustomerProfile(
        randomCustomerProfile.id,
        randomCustomerProfile.version
      )
      .then(customerProfile =>
        expect(customerProfile).toEqual(randomCustomerProfile)
      ));
  it("readVersion", () =>
    client
      .avroReadCustomerProfile(randomCustomerProfile.id)
      .then(customerProfile =>
        expect(customerProfile).toEqual(randomCustomerProfile)
      ));
});
