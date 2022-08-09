import { Link } from 'react-router-dom';
import styled from 'styled-components';

export const Container = styled.article`
  display: flex;
  gap: 1rem;
  border-radius: 0.375rem;
  padding: 1rem;
  width: 100%;
  height: max-content;
`;

export const ProductArea = styled(Link)`
  width: 35%;
  display: flex;
  flex-direction: column;
  gap: 1rem;

  &:hover {
    img {
      transform: scale(1.03);
      transition: 0.2s;
    }
    p {
      text-decoration: underline;
    }
  }
`;

export const ImageWrapper = styled.div`
  width: 100%;
  border: 1px solid ${({ theme }) => theme.colors.secondary};
  aspect-ratio: 1 / 1;

  display: flex;
  align-items: center;

  background-color: #fff;

  overflow: hidden;
`;

export const Image = styled.img`
  width: 100%;
`;

export const Title = styled.p`
  line-height: 1.3;
`;

export const ReviewArea = styled.div<{ isFull: boolean }>`
  display: flex;
  flex-direction: column;
  gap: 1rem;
  width: ${({ isFull }) => (isFull ? '100%' : '60%')};
`;

export const FlexColumnWrapper = styled(ReviewArea)`
  gap: 1rem;
`;

export const Wrapper = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
`;

export const UserWrapper = styled.div`
  display: flex;
  gap: 0.3rem;
`;

export const ProfileLink = styled(Link)`
  &:hover {
    p {
      text-decoration: underline;
    }
  }
`;

export const EditButton = styled.button`
  border: none;
  width: 2.5rem;
  border-radius: 0.5rem;
  background-color: ${({ theme }) => theme.colors.secondary};
`;

export const DeleteButton = styled(EditButton)``;

export const Content = styled.p`
  line-height: 1.4;
`;
