import useLogin from '@/hooks/useLogin';
import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

const params = new URLSearchParams(location.search);
const code = params.get('code');
export const authURI = `http://localhost:8080/api/v1/login`;

function Login() {
  const userData = useLogin(authURI, code);
  const navigate = useNavigate();

  useEffect(() => {
    if (userData?.accessToken) {
      if (userData?.jobType === null || userData?.career === null) {
        navigate('/register');
      } else {
        navigate('/');
      }
    }
  }, [userData]);

  return <div>로그인 진행중..</div>;
}

export default Login;
