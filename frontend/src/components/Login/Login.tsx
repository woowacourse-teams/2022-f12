import ROUTES from '@/constants/routes';
import useAuth from '@/hooks/useAuth';
import { useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';

function Login() {
  const { login } = useAuth();
  const { code } = useParams();

  const navigate = useNavigate();

  useEffect(() => {
    login(code)
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
