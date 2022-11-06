import { useState } from 'react';

import SearchBar from '@/components/common/SearchBar/SearchBar';

export default {
  component: SearchBar,
  title: 'Components/Common/SearchBar',
};

const Template = () => {
  const [input, setInput] = useState('');
  return <SearchBar searchInput={input} setSearchInput={setInput} />;
};

export const Default = () => <Template />;
