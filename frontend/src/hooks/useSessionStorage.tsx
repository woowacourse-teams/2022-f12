import { useMemo, useState } from 'react';

type Return<T> = [T, (T) => void, () => void];

function useSessionStorage<T>(key: string): Return<T> {
  const initialData = useMemo(
    () => (JSON.parse(window.sessionStorage.getItem(key)) as T) || null,
    []
  );
  const [data, setData] = useState<T | null>(initialData);

  const setStoredData = (newData: T) => {
    window.sessionStorage.setItem(key, JSON.stringify(newData));

    setData(newData);
  };

  const removeStoredData = () => {
    window.sessionStorage.removeItem(key);

    setData(null);
  };

  return [data, setStoredData, removeStoredData];
}

export default useSessionStorage;
