import useAuth from '@/hooks/useAuth';
import ProtectedRoute from '@/pages/common/ProtectedRoute/ProtectedRoutes';

function NonUserRoutes() {
  const { isLoggedIn } = useAuth();
  return <ProtectedRoute condition={!isLoggedIn} />;
}

export default NonUserRoutes;
