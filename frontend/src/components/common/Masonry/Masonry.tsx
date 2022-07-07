import React from 'react';

import * as S from './Masonry.style';

type Props = { columnCount: number; children: React.ReactNode };

function Masonry({ columnCount, children }: Props) {
  const columnArray = Array.from({
    length: columnCount,
  }).reduce<React.ReactNode[][]>((acc) => [...acc, []], []);

  React.Children.forEach(children, (child, index) => {
    columnArray[index % columnCount].push(child);
  });

  const childColumns = columnArray.map((column: React.ReactNode[], index) => {
    return <S.Column key={index}>{column}</S.Column>;
  });

  return <S.Container>{childColumns}</S.Container>;
}

export default Masonry;
