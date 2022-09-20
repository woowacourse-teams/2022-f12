import { useContext } from 'react';
import { useNavigate } from 'react-router-dom';

import {
  IsLoggedInContext,
  LogoutContext,
  SetUserDataContext,
} from '@/contexts/LoginContextProvider';

import useGet from '@/hooks/api/useGet';
import useModal from '@/hooks/useModal';

import { ENDPOINTS } from '@/constants/api';
import {
  FAILURE_MESSAGES,
  SUCCESS_MESSAGES,
  CONFIRM_MESSAGES,
} from '@/constants/messages';
import ROUTES from '@/constants/routes';

type Return = {
  login: (code: string) => Promise<void>;
  logout: () => void;
  isLoggedIn: boolean;
  revalidate: () => void;
};

function useAuth(): Return {
  const setUserData = useContext(SetUserDataContext);
  const handleLogout = useContext(LogoutContext);
  const isLoggedIn = useContext(IsLoggedInContext);
  const { showAlert, getConfirm } = useModal();

  const fetchUserData = useGet<UserData>({ url: ENDPOINTS.LOGIN });
  const fetchAccessToken = useGet<{ accessToken: string }>({
    url: ENDPOINTS.ISSUE_ACCESS_TOKEN,
  });
  const fetchMyData = useGet<Member>({ url: ENDPOINTS.ME });

  const navigate = useNavigate();

  const login = async (code: string) => {
    if (!code) {
      await showAlert(FAILURE_MESSAGES.LOGIN_CANCELED);
      navigate(ROUTES.HOME);
      return;
    }

    try {
      const userData = await fetchUserData({ params: { code } });
      setUserData(userData);
    } catch {
      throw new Error('로그인 오류');
    }
  };

  const logout = async () => {
    const confirmation = await getConfirm(CONFIRM_MESSAGES.LOGOUT);
    if (confirmation) {
      try {
        handleLogout();
        await showAlert(SUCCESS_MESSAGES.LOGOUT);
      } catch {
        await showAlert(FAILURE_MESSAGES.LOGOUT);
      }
    }
  };

  const revalidate = async () => {
    const { accessToken: token } = await fetchAccessToken();
    const { careerLevel, jobType, ...memberData } = await fetchMyData({ token });

    const registerCompleted = careerLevel !== null && jobType !== null;
    const userData: UserData = { member: memberData, token, registerCompleted };

    setUserData(userData);
  };

  return { login, logout, isLoggedIn, revalidate };
}

export default useAuth;
