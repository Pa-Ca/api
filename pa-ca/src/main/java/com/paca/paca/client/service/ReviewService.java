package com.paca.paca.client.service;

import com.paca.paca.branch.model.Branch;
import com.paca.paca.client.model.Client;
import com.paca.paca.client.model.Review;
import com.paca.paca.client.dto.ReviewDTO;
import com.paca.paca.client.model.ReviewLike;
import com.paca.paca.client.utils.ReviewMapper;
import com.paca.paca.client.repository.ClientRepository;
import com.paca.paca.client.repository.ReviewRepository;
import com.paca.paca.branch.repository.BranchRepository;
import com.paca.paca.client.repository.ReviewLikeRepository;
import com.paca.paca.exception.exceptions.ConflictException;
import com.paca.paca.exception.exceptions.NotFoundException;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewMapper reviewMapper;

    private final ReviewRepository reviewRepository;

    private final BranchRepository branchRepository;

    private final ClientRepository clientRepository;

    private final ReviewLikeRepository reviewLikeRepository;

    public ReviewDTO getById(Long id) throws NotFoundException {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "Review with id " + id + " does not exists",
                        35));

        ReviewDTO dto = reviewMapper.toDTO(review);
        dto.setLikes(reviewLikeRepository.findAllByReviewId(review.getId()).size());
        return dto;
    }

    public ReviewDTO save(ReviewDTO dto) throws NotFoundException, ConflictException {
        Optional<Client> client = clientRepository.findById(dto.getClientId());
        if (client.isEmpty()) {
            throw new NotFoundException(
                    "Client with id " + dto.getClientId() + " does not exists",
                    28);
        }
        Optional<Branch> branch = branchRepository.findById(dto.getBranchId());
        if (branch.isEmpty()) {
            throw new NotFoundException(
                    "Branch with id " + dto.getBranchId() + " does not exists",
                    20);
        }
        Boolean exists = reviewRepository.findByClientIdAndBranchId(dto.getClientId(), dto.getBranchId()).isPresent();
        if (exists) {
            throw new ConflictException(
                    "Review already exists",
                    39);
        }

        Review newReview = reviewMapper.toEntity(dto, client.get(), branch.get());
        newReview = reviewRepository.save(newReview);

        ReviewDTO dtoResponse = reviewMapper.toDTO(newReview);

        return dtoResponse;
    }

    public ReviewDTO update(Long id, ReviewDTO dto) throws NotFoundException {
        Optional<Review> current = reviewRepository.findById(id);
        if (current.isEmpty()) {
            throw new NotFoundException(
                    "Review with id " + id + " does not exists",
                    35);
        }

        Review newReview = reviewMapper.updateModel(dto, current.get());
        newReview = reviewRepository.save(newReview);
        ReviewDTO dtoResponse = reviewMapper.toDTO(newReview);

        return dtoResponse;
    }

    public void delete(Long id) throws NotFoundException {
        Optional<Review> current = reviewRepository.findById(id);
        if (current.isEmpty()) {
            throw new NotFoundException(
                    "Review with id " + id + " does not exists",
                    35);
        }
        reviewRepository.deleteById(id);
    }

    public ReviewDTO like(Long id, Long clientId) throws NotFoundException, ConflictException {
        Optional<Review> review = reviewRepository.findById(id);
        if (review.isEmpty()) {
            throw new NotFoundException(
                    "Review with id " + id + " does not exists",
                    35);
        }
        Optional<Client> client = clientRepository.findById(clientId);
        if (client.isEmpty()) {
            throw new NotFoundException(
                    "Client with id " + clientId + " does not exists",
                    28);
        }
        Boolean exists = reviewLikeRepository.existsByClientIdAndReviewId(clientId, id);
        if (exists) {
            throw new ConflictException(
                    "Review like already exists",
                    37);
        }

        ReviewLike like = ReviewLike.builder()
                .client(client.get())
                .review(review.get())
                .build();
        reviewLikeRepository.save(like);

        ReviewDTO dto = reviewMapper.toDTO(review.get());
        dto.setLikes(reviewLikeRepository.findAllByReviewId(review.get().getId()).size());
        return dto;
    }

    public ReviewDTO dislike(Long id, Long clientId) throws NotFoundException {
        Optional<Review> review = reviewRepository.findById(id);
        if (review.isEmpty()) {
            throw new NotFoundException(
                    "Review with id " + id + " does not exists",
                    35);
        }
        Optional<Client> client = clientRepository.findById(clientId);
        if (client.isEmpty()) {
            throw new NotFoundException(
                    "Client with id " + clientId + " does not exists",
                    28);
        }
        Optional<ReviewLike> like = reviewLikeRepository.findByClientIdAndReviewId(clientId, id);
        if (like.isEmpty()) {
            throw new NotFoundException(
                    "Review like does not exists",
                    38);
        }

        reviewLikeRepository.delete(like.get());

        ReviewDTO dto = reviewMapper.toDTO(review.get());
        dto.setLikes(reviewLikeRepository.findAllByReviewId(review.get().getId()).size());
        return dto;
    }

}
