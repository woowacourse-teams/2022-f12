import ROUTES from '@/constants/routes';
import { Navigate, Outlet } from 'react-router-dom';

type Routes = keyof typeof ROUTES;
type Props = {
  condition: boolean;
  redirectPath?: typeof ROUTES[Routes];
};

function ProtectedRoute({ condition, redirectPath = ROUTES.HOME }: Props) {
  if (!condition) {
    return <Navigate to={redirectPath} replace />;
  }

  return <Outlet />;
}

export default ProtectedRoute;
