import { useRoutes } from 'react-router-dom';
import { PAGES } from '@/pages';

function App() {
  const pages = useRoutes(PAGES);
  return pages;
}

export default App;
