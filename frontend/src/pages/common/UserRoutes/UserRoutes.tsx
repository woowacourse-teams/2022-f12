import useAuth from '@/hooks/useAuth';
import ProtectedRoute from '@/pages/common/ProtectedRoute/ProtectedRoutes';

function UserRoutes() {
  const { isLoggedIn: isUser } = useAuth();

  return <ProtectedRoute when={isUser} />;
}

export default UserRoutes;
