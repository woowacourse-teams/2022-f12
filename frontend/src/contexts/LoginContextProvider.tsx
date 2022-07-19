import useSessionStorage from '@/hooks/useSessionStorage';
import { createContext, PropsWithChildren, useEffect, useState } from 'react';

const initialState: UserData = {
  token: null,
  jobType: null,
  career: null,
};

export const IsLoggedInContext = createContext<boolean>(false);
export const UserDataContext = createContext(initialState);
export const SetUserDataContext = createContext<React.Dispatch<
  React.SetStateAction<UserData>
> | null>(null);
export const LogoutContext = createContext<() => void | null>(null);

function LoginContextProvider({ children }: PropsWithChildren) {
  const [userData, setUserData, removeUserData] =
    useSessionStorage<UserData>('userData');
  const [isLoggedIn, setLoggedIn] = useState(true);

  useEffect(() => {
    if (userData && userData.token) {
      setLoggedIn(true);
      return;
    }

    setLoggedIn(false);
  }, [userData]);

  return (
    <IsLoggedInContext.Provider value={isLoggedIn}>
      <UserDataContext.Provider value={userData}>
        <SetUserDataContext.Provider value={setUserData}>
          <LogoutContext.Provider value={removeUserData}>
            {children}
          </LogoutContext.Provider>
        </SetUserDataContext.Provider>
      </UserDataContext.Provider>
    </IsLoggedInContext.Provider>
  );
}

export default LoginContextProvider;
