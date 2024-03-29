import { Navigate, Outlet } from 'react-router-dom';

import ROUTES from '@/constants/routes';

type Routes = keyof typeof ROUTES;
type Props = {
  when: boolean;
  redirectPath?: typeof ROUTES[Routes];
};

function ProtectedRoute({ when, redirectPath = ROUTES.HOME }: Props) {
  if (!when) {
    return <Navigate to={redirectPath} replace />;
  }

  return <Outlet />;
}

export default ProtectedRoute;
