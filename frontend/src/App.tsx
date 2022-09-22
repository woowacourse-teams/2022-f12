import { PAGES } from '@/pages';
import { useRoutes } from 'react-router-dom';

function App() {
  const pages = useRoutes(PAGES);
  return pages;
}

export default App;
