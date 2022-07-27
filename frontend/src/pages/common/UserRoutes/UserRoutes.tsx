import useAuth from '@/hooks/useAuth';
import ProtectedRoute from '@/pages/common/ProtectedRoute/ProtectedRoutes';

function UserRoutes() {
  const { isLoggedIn } = useAuth();
  return <ProtectedRoute condition={isLoggedIn} />;
}

export default UserRoutes;
