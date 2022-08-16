import styled from 'styled-components';

export const Container = styled.article<{ animationTrigger: boolean; index: number }>`
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  gap: 1rem;
  width: 19rem;
  border-radius: 0.375rem;
  padding: 1rem;

  &:hover {
    img {
      transform: scale(1.03);
      transition: 0.2s;
    }
    h2 {
      text-decoration: underline;
    }
  }
  ${({ animationTrigger }) =>
    !animationTrigger && 'transform : translateY(-10px); scale: 1.1; opacity: 0;'}
  transition: 300ms ${({ index }) => (index + 1) * 100}ms;
`;

export const ImageWrapper = styled.div`
  width: 100%;
  overflow: hidden;
  border: 1px solid ${({ theme }) => theme.colors.secondary};
  background-color: #fff;
  display: flex;
  align-items: center;
`;

export const Image = styled.img`
  width: 100%;
  object-fit: cover;
`;

export const BottomWrapper = styled.div`
  display: flex;
  justify-content: space-between;
`;

export const Name = styled.h2`
  font-size: 1rem;
  line-height: 1.3;
`;
