import ProtectedRoute from '@/pages/common/ProtectedRoute/ProtectedRoutes';

import useAuth from '@/hooks/useAuth';

function NonUserRoutes() {
  const { isLoggedIn } = useAuth();
  const isNonUser = !isLoggedIn;

  return <ProtectedRoute when={isNonUser} />;
}

export default NonUserRoutes;
