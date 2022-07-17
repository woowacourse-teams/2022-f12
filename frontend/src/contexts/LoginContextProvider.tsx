import { createContext, PropsWithChildren, useEffect, useState } from 'react';

const accessToken = window.sessionStorage.getItem('accessToken') || null;
const jobType = window.sessionStorage.getItem('jobType') || null;
const career = window.sessionStorage.getItem('career') || null;

const initialState = {
  accessToken,
  jobType,
  career,
};

const logoutState = {
  accessToken: null,
  jobType: null,
  career: null,
};

export const IsLoggedInContext = createContext(!!accessToken);
export const UserDataContext = createContext(initialState);
export const SetUserDataContext = createContext<React.Dispatch<
  React.SetStateAction<typeof initialState>
> | null>(null);
export const LogOutContext = createContext<() => void | null>(null);

function LoginContextProvider({ children }: PropsWithChildren) {
  const [isLoggedIn, setLoggedIn] = useState(false);
  const [userData, setUserData] = useState(initialState);

  const handleUserDataSet = (userDataInput: typeof initialState) => {
    const { accessToken, jobType, career } = userDataInput;
    window.sessionStorage.setItem('accessToken', accessToken);
    window.sessionStorage.setItem('jobType', jobType);
    window.sessionStorage.setItem('career', career);

    setUserData(userDataInput);
  };

  const logout = () => {
    window.sessionStorage.removeItem('accessToken');
    window.sessionStorage.removeItem('jobType');
    window.sessionStorage.removeItem('career');

    setUserData(logoutState);
  };

  useEffect(() => {
    if (userData && userData.accessToken && !isLoggedIn) {
      setLoggedIn(true);
      return;
    }
    setLoggedIn(false);
  }, [userData]);

  return (
    <IsLoggedInContext.Provider value={isLoggedIn}>
      <UserDataContext.Provider value={userData}>
        <SetUserDataContext.Provider value={handleUserDataSet}>
          <LogOutContext.Provider value={logout}>
            {children}
          </LogOutContext.Provider>
        </SetUserDataContext.Provider>
      </UserDataContext.Provider>
    </IsLoggedInContext.Provider>
  );
}

export default LoginContextProvider;
