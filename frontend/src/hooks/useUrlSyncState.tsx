import { useEffect, useState } from 'react';
import { useLocation, useSearchParams } from 'react-router-dom';

const useUrlSyncState: (
  key: string,
  defaultValue?: string
) => [string, React.Dispatch<React.SetStateAction<string>>] = (
  key,
  defaultValue = null
) => {
  const location = useLocation();
  const [searchParams, setSearchParams] = useSearchParams();

  const initialState = searchParams.get(key)
    ? decodeURIComponent(searchParams.get(key))
    : defaultValue;

  const [value, setValue] = useState<string>(initialState);

  // setState가 아니라 뒤로가기 등 상황에서 UI 동기화
  // 컴포넌트가 navigate 되지 않아 처리해주지 않으면 초기값 지정이 자동으로 되지 않음
  useEffect(() => {
    setValue(searchParams.get(key) || defaultValue);
  }, [location.key]);

  useEffect(() => {
    if (searchParams.get(key) === value) return;

    if (value === null) {
      searchParams.delete(key);
    } else {
      searchParams.set(key, value);
    }

    setSearchParams(searchParams);
  }, [value]);

  return [value, setValue];
};

export default useUrlSyncState;
