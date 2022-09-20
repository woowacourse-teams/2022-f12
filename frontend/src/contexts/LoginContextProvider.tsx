import { createContext, PropsWithChildren, useEffect, useState } from 'react';

const initialState: UserData = {
  member: {
    id: null,
    gitHubId: null,
    imageUrl: null,
    name: null,
    followerCount: null,
    following: null,
  },
  registerCompleted: false,
  token: null,
};

export const IsLoggedInContext = createContext<boolean>(false);
export const UserDataContext = createContext(initialState);
export const SetUserDataContext = createContext<React.Dispatch<
  React.SetStateAction<UserData>
> | null>(null);
export const LogoutContext = createContext<() => void | null>(null);

function LoginContextProvider({ children }: PropsWithChildren) {
  const [userData, setUserData] = useState<UserData>(null);
  const checkLoginStatus = () => userData && !!userData.token;

  const [isLoggedIn, setLoggedIn] = useState<boolean>(checkLoginStatus());

  const handleLogout = () => {
    setUserData(null);
    // 로그아웃 시 바로 false로 변경하지 않고 아래 이펙트 훅에 의존하면 상태가 변경되기 전 registeredCompleted 루트 때문에 오류 발생
    setLoggedIn(false);
  };

  useEffect(() => {
    setLoggedIn(checkLoginStatus());
  }, [userData]);

  return (
    <IsLoggedInContext.Provider value={isLoggedIn}>
      <UserDataContext.Provider value={userData}>
        <SetUserDataContext.Provider value={setUserData}>
          <LogoutContext.Provider value={handleLogout}>{children}</LogoutContext.Provider>
        </SetUserDataContext.Provider>
      </UserDataContext.Provider>
    </IsLoggedInContext.Provider>
  );
}

export default LoginContextProvider;
