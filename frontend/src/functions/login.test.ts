import { login, logout, getUser } from "./login";

export class FakeLocalStorage {
  public store = {};

  public clear() {
    this.store = {};
  }

  public getItem(key: string) {
    return this.store[key] || null;
  }

  public setItem(key: string, value: string) {
    this.store[key] = value;
  }

  public removeItem(key: string) {
    delete this.store[key];
  }
}

let localStorage;

describe("Login takes email address and stores in in localstorage", () => {
  test("login", () => {
    localStorage = new FakeLocalStorage();

    const result = login(localStorage)("test@test.dev");

    expect(result).toMatchObject({
      username: "test@test.dev",
      status: "success",
      login: true,
      error: null
    });
  });
});

test("logout", () => {
  localStorage = new FakeLocalStorage();

  const loginuser = login(localStorage)("test@test.dev");

  expect(loginuser.login).toBe(true);

  const result = logout(localStorage);

  expect(result).toMatchObject({
    error: null,
    login: false,
    status: "logged out"
  });
});

describe("get user", () => {
  test("not logged in", () => {
    localStorage = new FakeLocalStorage();
    const result = getUser(localStorage);

    expect(result.status).toBe("No user logged in");
  });

  test("if a user is logged in", () => {
    localStorage = new FakeLocalStorage();
    const loginuser = login(localStorage)("test@test.dev");

    expect(loginuser.login).toBe(true);
    expect(loginuser.username).toBe("test@test.dev");
  });
});

describe("working out localstorage fluff", () => {
  test("should save to localStorage", () => {
    localStorage.clear();
    const KEY = "email";
    const VALUE = "bar";

    localStorage.setItem(KEY, VALUE);

    expect(localStorage.getItem(KEY)).toBe("bar");
  });
  test("should not save to localStorage", () => {
    localStorage.clear();
    const KEY = "email";
    const VALUE = "bar";

    // NOT HAPPENING localStorage.setItem(KEY, VALUE);

    expect(localStorage.getItem(KEY)).not.toBe("bar");
  });
});

describe("clear localStorage", () => {
  beforeAll(() => {
    localStorage.clear();
    localStorage.setItem("key", "delete me!!");
  });
  test("should clear localstorage", () => {
    expect(localStorage.getItem("key")).toBe("delete me!!");
    localStorage.clear();
    expect(localStorage.getItem("key")).toBe(null);
  });
});

describe("add items and remove an item", () => {
  beforeAll(() => {
    localStorage.clear();
    const items = [
      { KEY: "one", VALUE: "one@test.dev" },
      { KEY: "two", VALUE: "tow@test.dev" }
    ];

    items.map(x => localStorage.setItem(x.KEY, x.VALUE));
  });
  test("remove item", () => {
    expect(localStorage.store).toEqual({
      one: "one@test.dev",
      two: "tow@test.dev"
    });
    expect(localStorage.getItem("one")).toEqual("one@test.dev");
    localStorage.removeItem("one");
    expect(localStorage.getItem("one")).toEqual(null);
    expect(localStorage.store).toEqual({ two: "tow@test.dev" });
  });
});
