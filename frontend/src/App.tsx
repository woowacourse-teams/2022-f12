import { PAGES } from '@/pages';
import { useEffect } from 'react';
import { useRoutes } from 'react-router-dom';

import useAuth from '@/hooks/useAuth';

function App() {
  const pages = useRoutes(PAGES);
  const { revalidate } = useAuth();

  useEffect(() => {
    revalidate();
  }, []);

  return pages;
}

export default App;
