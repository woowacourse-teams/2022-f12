import ProtectedRoute from '@/pages/common/ProtectedRoute/ProtectedRoutes';

import useAuth from '@/hooks/useAuth';

function UserRoutes() {
  const { isLoggedIn: isUser } = useAuth();

  console.log('asdfkahsdlfkasdhlkfasjdklf');
  console.log(isUser);

  return <ProtectedRoute when={isUser} />;
}

export default UserRoutes;
