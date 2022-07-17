import useLogin from '@/hooks/useLogin';
// import { useEffect } from 'react';
import { Navigate } from 'react-router-dom';

const params = new URLSearchParams(location.search);
const code = params.get('code');
export const authURI = `http://localhost:8080/api/v1/login`;

function Login() {
  const userData = useLogin(authURI, code);

  return userData && userData.accessToken ? (
    <Navigate to={'/'} />
  ) : (
    <div>로그인 진행중..</div>
  );
}

export default Login;
