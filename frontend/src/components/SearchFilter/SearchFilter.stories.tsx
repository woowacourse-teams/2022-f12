import SearchFilter from '@/components/SearchFilter/SearchFilter';
import { useState } from 'react';

export default {
  component: SearchFilter,
  title: 'Components/SearchFilter',
};

const Template = () => {
  const [value, setValue] = useState('');
  return (
    <SearchFilter
      value={value}
      setValue={setValue}
      title={'예시'}
      options={{ 예시1: '예시1', 예시2: '예시2' }}
    />
  );
};

export const Default = () => <Template />;
