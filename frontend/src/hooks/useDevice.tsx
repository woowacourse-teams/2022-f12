import { useContext } from 'react';

import { DeviceContext, DisplayWidthContext } from '@/contexts/DeviceContextProvider';

function useDevice() {
  const device = useContext(DeviceContext) || 'desktop';
  const displayWidth = useContext(DisplayWidthContext) || 1440;

  return { device, displayWidth };
}

export default useDevice;
