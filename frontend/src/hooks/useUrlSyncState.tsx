import { useEffect, useState } from 'react';
import { useLocation, useSearchParams } from 'react-router-dom';

const useUrlSyncState = <T extends string>(
  key: string,
  defaultValue?: T
): [T, React.Dispatch<React.SetStateAction<T>>] => {
  const realDefaultValue = defaultValue || null;
  const location = useLocation();
  const [searchParams, setSearchParams] = useSearchParams();

  const initialState = searchParams.get(key)
    ? decodeURIComponent(searchParams.get(key))
    : realDefaultValue;

  // TODO: 만약 url의 초기값이 T가 아닌 경우를 대비해서 throw를 하고 ErrorBoundary를 설정할 것을 추후에 고민
  const [value, setValue] = useState<T>(initialState as T);

  // setState가 아니라 뒤로가기 등 상황에서 UI 동기화
  // 컴포넌트가 navigate 되지 않아 처리해주지 않으면 초기값 지정이 자동으로 되지 않음
  useEffect(() => {
    setValue((searchParams.get(key) as T) || realDefaultValue);
  }, [location.key]);

  useEffect(() => {
    if (searchParams.get(key) === value) return;

    if (value === undefined || value === null) {
      searchParams.delete(key);
    } else {
      searchParams.set(key, value);
    }

    setSearchParams(searchParams);
  }, [value]);

  return [value, setValue];
};

export default useUrlSyncState;
