import styled from 'styled-components';

export const Container = styled.article`
  display: flex;
  gap: 1rem;
  border-radius: 0.375rem;
  padding: 1rem;
  width: 100%;
  height: max-content;
`;

export const ProductArea = styled.div`
  width: 35%;
  display: flex;
  flex-direction: column;
  gap: 1rem;
`;

export const ImageWrapper = styled.div`
  width: 100%;
  border: 1px solid ${({ theme }) => theme.colors.secondary};
  aspect-ratio: 1 / 1;

  display: flex;
  align-items: center;

  background-color: #fff;
`;
export const Image = styled.img`
  width: 100%;
`;

export const Title = styled.div`
  line-height: 1.3;
`;

export const ReviewArea = styled.div<{ isFull: boolean }>`
  display: flex;
  flex-direction: column;
  gap: 1rem;
  width: ${({ isFull }) => (isFull ? '100%' : '60%')};
`;

export const Wrapper = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
`;

export const Content = styled.p`
  line-height: 1.4;
`;
