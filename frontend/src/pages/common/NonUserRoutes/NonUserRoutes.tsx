import useAuth from '@/hooks/useAuth';
import ProtectedRoute from '@/pages/common/ProtectedRoute/ProtectedRoutes';

function NonUserRoutes() {
  const { isLoggedIn } = useAuth();
  const isNonUser = !isLoggedIn;

  return <ProtectedRoute when={isNonUser} />;
}

export default NonUserRoutes;
