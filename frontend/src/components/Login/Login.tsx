import { useContext, useEffect } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';

import Loading from '@/components/common/Loading/Loading';

import { UserDataContext } from '@/contexts/LoginContextProvider';

import useAuth from '@/hooks/useAuth';
import useModal from '@/hooks/useModal';

import ROUTES from '@/constants/routes';

function Login() {
  const { login } = useAuth();
  const { showAlert } = useModal();
  const [searchParam] = useSearchParams();
  const userData = useContext(UserDataContext);

  const navigate = useNavigate();

  useEffect(() => {
    const githubCode = searchParam.get('code');

    login(githubCode).catch(async () => {
      await showAlert('로그인에 실패했습니다. 잠시 후 다시 시도해주세요.');
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
