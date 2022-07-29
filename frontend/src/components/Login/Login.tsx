import Loading from '@/components/common/Loading/Loading';
import ROUTES from '@/constants/routes';
import { UserDataContext } from '@/contexts/LoginContextProvider';
import useAuth from '@/hooks/useAuth';
import { useContext, useEffect } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';

function Login() {
  const { login } = useAuth();
  const [searchParam] = useSearchParams();
  const userData = useContext(UserDataContext);

  const navigate = useNavigate();

  useEffect(() => {
    login(searchParam.get('code')).catch(() => {
      alert('로그인에 실패했습니다. 잠시 후 다시 시도해주세요.');
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
