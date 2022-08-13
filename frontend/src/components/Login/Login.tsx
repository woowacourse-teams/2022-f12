import { useContext, useEffect } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';

import Loading from '@/components/common/Loading/Loading';

import { UserDataContext } from '@/contexts/LoginContextProvider';

import useAuth from '@/hooks/useAuth';

import ROUTES from '@/constants/routes';

function Login() {
  const { login } = useAuth();
  const [searchParam] = useSearchParams();
  const userData = useContext(UserDataContext);

  const navigate = useNavigate();

  useEffect(() => {
    const githubCode = searchParam.get('code');

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
