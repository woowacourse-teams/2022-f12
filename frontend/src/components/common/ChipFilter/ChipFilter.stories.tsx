import { useState, PropsWithChildren } from 'react';

import ChipFilter from '@/components/common/ChipFilter/ChipFilter';

export default {
  component: ChipFilter,
  title: 'Components/Common/ChipFilter',
};

type Props = {
  buttonValue: string;
  initFilter: string;
};

const Template = ({ buttonValue, initFilter, children }: PropsWithChildren<Props>) => {
  const [filter, setFilter] = useState(initFilter);
  return (
    <ChipFilter
      fontSize={10}
      value={buttonValue}
      filter={filter}
      handleClick={() => {
        if (buttonValue === filter) {
          setFilter('other');
          return;
        }
        setFilter(buttonValue);
      }}
    >
      {children}
    </ChipFilter>
  );
};

export const Clicked = () => (
  <Template buttonValue="none" initFilter="none">
    경력 없음
  </Template>
);

export const unClicked = () => (
  <Template buttonValue="none" initFilter="other">
    경력 없음
  </Template>
);
