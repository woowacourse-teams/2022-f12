import React from 'react';

import * as S from './Masonry.style';

type Props = { columnCount: number; children: React.ReactNode };

function Masonry({ columnCount, children }: Props) {
  const childrenArray = React.Children.toArray(children);

  const childrenComponents = childrenArray.reduce<React.ReactNode[][]>(
    (acc, child, index) => {
      const column = index % columnCount;
      const currentArray = [...acc];
      if (currentArray[column] === undefined) {
        currentArray.push([child]);
      } else {
        currentArray[column].push(child);
      }

      return currentArray;
    },
    []
  );

  const childColumns = childrenComponents.map((column, index) => {
    return <S.Column key={index}>{column}</S.Column>;
  });

  return <S.Container>{childColumns}</S.Container>;
}

export default Masonry;
