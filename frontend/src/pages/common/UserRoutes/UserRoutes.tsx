import ProtectedRoute from '@/pages/common/ProtectedRoute/ProtectedRoutes';

import useAuth from '@/hooks/useAuth';

function UserRoutes() {
  const { isLoggedIn: isUser } = useAuth();

  return <ProtectedRoute when={isUser} />;
}

export default UserRoutes;
