import { useEffect, useState } from 'react';

function useAnimation(condition: boolean): [boolean, () => void, boolean] {
  const [isComplete, setComplete] = useState(false);

  useEffect(() => {
    if (condition) {
      setComplete(true);
    }
  }, [condition]);

  const shouldRender = condition || isComplete;
  const animationTrigger = condition && isComplete;

  const handleUnmount = () => {
    if (!condition) setComplete(false);
  };

  return [shouldRender, handleUnmount, animationTrigger];
}

export default useAnimation;
