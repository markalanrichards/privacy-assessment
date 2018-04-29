export interface LocalStorageFN<T> {
  setItem(index: string, item: T): void;
  getItem(index: string): T;
  clear(): void;
}
interface LoginInterface {
  username: string;
  status: string;
  login: boolean;
  error: null;
}
export const login = (localStorageFn: LocalStorageFN<string>) => (
  username: string
): LoginInterface => {
  localStorageFn.setItem("username", username);
  return {
    username: localStorageFn.getItem("username"),
    status: "success",
    login: true,
    error: null
  };
};

interface LogoutInterface {
  status: string;
  login: boolean;
  error: null;
}

export const logout = (
  localStorageFn: LocalStorageFN<string>
): LogoutInterface => {
  localStorageFn.clear();
  return {
    status: "logged out",
    login: false,
    error: null
  };
};

interface GetUserInterface {
  username: string;
  status: string;
  login: boolean;
  error: null;
}

export const getUser = (
  localStorageFn: LocalStorageFN<string>
): GetUserInterface => {
  const isTheUserLogedIn = localStorageFn.getItem("username");
  return isTheUserLogedIn === null || "undefined"
    ? {
        username: null,
        status: "No user logged in",
        login: false,
        error: null
      }
    : {
        username: isTheUserLogedIn,
        status: "User is logged in",
        login: true,
        error: null
      };
};
