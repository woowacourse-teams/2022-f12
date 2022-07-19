import ROUTES from '@/constants/routes';
import useAuth from '@/hooks/useAuth';
import { useEffect } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';

function Login() {
  const { login } = useAuth();
  const [searchParam] = useSearchParams();

  const navigate = useNavigate();

  useEffect(() => {
    login(searchParam.get('code'))
      .catch(() => {
        alert('로그인에 실패했습니다. 잠시 후 다시 시도해주세요.');
      })
      .finally(() => {
        navigate(ROUTES.HOME);
      });
  }, []);

  return <div>로그인 진행중..</div>;
}

export default Login;
