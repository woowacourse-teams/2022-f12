import { ENDPOINTS } from '@/constants/api';
import {
  IsLoggedInContext,
  LogoutContext,
  SetUserDataContext,
} from '@/contexts/LoginContextProvider';
import useGet from '@/hooks/api/useGet';
import { useContext } from 'react';

type Return = {
  login: (githubCode: string) => Promise<void>;
  logout: () => void;
  isLoggedIn: boolean;
};

function useAuth(): Return {
  const setUserData = useContext(SetUserDataContext);
  const removeUserData = useContext(LogoutContext);
  const isLoggedIn = useContext(IsLoggedInContext);

  const fetchUserData = useGet<UserData>({ url: ENDPOINTS.LOGIN });

  const login = async (githubCode: string) => {
    const userData = await fetchUserData({ code: githubCode });

    setUserData(userData);
  };

  const logout = () => {
    removeUserData();
  };

  return { login, logout, isLoggedIn };
}

export default useAuth;
