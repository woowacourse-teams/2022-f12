import { useContext } from 'react';

import {
  IsLoggedInContext,
  LogoutContext,
  SetUserDataContext,
} from '@/contexts/LoginContextProvider';

import useGet from '@/hooks/api/useGet';
import useModal from '@/hooks/useModal';

import { ENDPOINTS } from '@/constants/api';

type Return = {
  login: (githubCode: string) => Promise<void>;
  logout: () => void;
  isLoggedIn: boolean;
};

function useAuth(): Return {
  const setUserData = useContext(SetUserDataContext);
  const handleLogout = useContext(LogoutContext);
  const isLoggedIn = useContext(IsLoggedInContext);
  const { showAlert, getConfirm } = useModal();

  const fetchUserData = useGet<UserData>({ url: ENDPOINTS.LOGIN });

  const login = async (githubCode: string) => {
    try {
      const userData = await fetchUserData({ code: githubCode });
      setUserData(userData);
    } catch (error) {
      if (error instanceof Error) {
        await showAlert(error.message);
        return;
      }

      await showAlert('알 수 없는 오류 발생');
    }
  };

  const logout = async () => {
    const confirmation = await getConfirm('로그아웃 하시겠습니까?');
    if (confirmation) {
      try {
        handleLogout();
        await showAlert('로그아웃이 완료되었습니다.');
      } catch {
        await showAlert('로그아웃에 실패했습니다. 다시 시도해주세요.');
      }
    }
  };

  return { login, logout, isLoggedIn };
}

export default useAuth;
