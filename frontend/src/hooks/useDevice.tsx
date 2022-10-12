import { useContext } from 'react';

import { DeviceContext, DisplayWidthContext } from '@/contexts/DeviceContextProvider';

function useDevice() {
  const device = useContext(DeviceContext);
  const displayWidth = useContext(DisplayWidthContext);

  return { device, displayWidth };
}

export default useDevice;
