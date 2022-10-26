import { useContext, useEffect } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';

import Loading from '@/components/common/Loading/Loading';

import { UserDataContext } from '@/contexts/LoginContextProvider';

import useAuth from '@/hooks/useAuth';
import useModal from '@/hooks/useModal';

import ROUTES from '@/constants/routes';
import SEARCH_PARAMS from '@/constants/searchParams';

function Login() {
  const { login } = useAuth();
  const [searchParam] = useSearchParams();
  const userData = useContext(UserDataContext);
  const { showAlert } = useModal();

  const navigate = useNavigate();

  useEffect(() => {
    const githubCode = searchParam.get(SEARCH_PARAMS.CODE);
    if (githubCode === null) {
      showAlert('유효하지 않은 로그인 코드입니다.').catch((e) => {
        console.log(e);
      });
      return;
    }
    login(githubCode).catch(() => {
      navigate(ROUTES.HOME);
    });
  }, []);

  useEffect(() => {
    if (!userData) return;

    if (userData.registerCompleted) {
      navigate(ROUTES.HOME);
      return;
    }
    navigate(ROUTES.REGISTER);
  }, [userData]);

  return <Loading />;
}

export default Login;
