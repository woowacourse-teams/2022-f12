import { useContext } from 'react';
import { useNavigate } from 'react-router-dom';

import {
  IsLoggedInContext,
  LogoutContext,
  SetUserDataContext,
} from '@/contexts/LoginContextProvider';

import useGet from '@/hooks/api/useGet';
import usePost from '@/hooks/api/usePost';
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
  revalidate: () => Promise<void>;
};

function useAuth(): Return {
  const setUserData = useContext(SetUserDataContext);
  const handleLogout = useContext(LogoutContext);
  const isLoggedIn = useContext(IsLoggedInContext);
  const { showAlert, getConfirm } = useModal();

  const fetchUserData = useGet<UserData>({ url: ENDPOINTS.LOGIN });
  const fetchAccessToken = usePost<unknown, { accessToken: string }>({
    url: ENDPOINTS.ISSUE_ACCESS_TOKEN,
  });
  const fetchMyData = useGet<Member>({ url: ENDPOINTS.ME });
  const requestLogout = useGet({ url: ENDPOINTS.LOGOUT });

  const navigate = useNavigate();

  const isError = (error: unknown): error is Error => {
    return error instanceof Error;
  };

  const login = async (code: string) => {
    if (!code) {
      await showAlert(FAILURE_MESSAGES.LOGIN_CANCELED);
      navigate(ROUTES.HOME);
      return;
    }

    try {
      const userData = await fetchUserData({ params: { code }, includeCookie: true });
      setUserData(userData);
    } catch {
      throw new Error('로그인 오류');
    }
  };

  const logout = async () => {
    const confirmation = await getConfirm(CONFIRM_MESSAGES.LOGOUT);
    if (!confirmation) return;

    try {
      await requestLogout({ includeCookie: true });
      handleLogout();
      await showAlert(SUCCESS_MESSAGES.LOGOUT);
    } catch {
      await showAlert(FAILURE_MESSAGES.LOGOUT);
    }
  };

  const revalidate = async () => {
    try {
      const response = await fetchAccessToken('', false);

      const { accessToken: token } = response;
      const { careerLevel, jobType, ...memberData } = await fetchMyData({ token });

      const registerCompleted = careerLevel !== null && jobType !== null;
      const userData: UserData = { member: memberData, token, registerCompleted };

      setUserData(userData);
    } catch (error) {
      if (isError(error) && error.message === FAILURE_MESSAGES.NO_REFRESH_TOKEN) {
        // 한 경우에만 silent 처리
        return;
      }
      console.log(error);
    }
  };
  return { login, logout, isLoggedIn, revalidate };
}

export default useAuth;
