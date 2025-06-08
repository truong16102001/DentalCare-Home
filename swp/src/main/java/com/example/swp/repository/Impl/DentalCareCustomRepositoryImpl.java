package com.example.swp.repository.Impl;

import com.example.swp.entity.Booking;
import com.example.swp.entity.Service;
import com.example.swp.repository.DentalCareCustomRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public class DentalCareCustomRepositoryImpl implements DentalCareCustomRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Service> searchServices(String key, String value, String type, int limit, int offset) {
        List<String> allowedSortFields = Arrays.asList("name", "description");
        List<String> allowedSortOrders = Arrays.asList("asc", "desc");

        StringBuilder jpql = new StringBuilder(
                "SELECT DISTINCT s FROM Service s " +
                        "LEFT JOIN FETCH s.serviceImages si " +
                        "LEFT JOIN FETCH si.image i " +
                        "WHERE 1=1 AND s.isActive = true "
        );

        if (key != null && !key.trim().isEmpty()) {
            jpql.append(" AND (");
            jpql.append("LOWER(s.serviceName) LIKE LOWER(:keyword) ");
            jpql.append("OR LOWER(s.description) LIKE LOWER(:keyword)");
            jpql.append(")");
        }

        String sortField = "serviceName";
        if ("name".equalsIgnoreCase(value)) {
            sortField = "serviceName";
        } else if ("description".equalsIgnoreCase(value)) {
            sortField = "description";
        }

        if (value != null && allowedSortFields.contains(value.toLowerCase())
                && type != null && allowedSortOrders.contains(type.toLowerCase())) {
            jpql.append(" ORDER BY s.").append(sortField).append(" ").append(type.toUpperCase());
        } else {
            jpql.append(" ORDER BY s.serviceName ASC");
        }

        TypedQuery<Service> query = entityManager.createQuery(jpql.toString(), Service.class);

        if (key != null && !key.trim().isEmpty()) {
            query.setParameter("keyword", "%" + key.trim() + "%");
        }

        query.setFirstResult(offset);
        query.setMaxResults(limit);

        // Tránh kết quả trùng lặp do join fetch
        List<Service> resultList = query.getResultList();

        return resultList;
    }

    @Override
    public int countServices(String key) {
        StringBuilder jpql = new StringBuilder("SELECT COUNT(s) FROM Service s WHERE 1=1 AND s.isActive = true ");

        if (key != null && !key.trim().isEmpty()) {
            jpql.append(" AND (");
            jpql.append("LOWER(s.serviceName) LIKE LOWER(:keyword) ");
            jpql.append("OR LOWER(s.description) LIKE LOWER(:keyword)");
            jpql.append(")");
        }

        TypedQuery<Long> query = entityManager.createQuery(jpql.toString(), Long.class);

        if (key != null && !key.trim().isEmpty()) {
            query.setParameter("keyword", "%" + key.trim() + "%");
        }

        return query.getSingleResult().intValue();
    }

    @Override
    public Service findById(int serviceId) {
        String jpql = "SELECT DISTINCT s FROM Service s " +
                "LEFT JOIN FETCH s.serviceImages si " +
                "LEFT JOIN FETCH si.image i " +
                "WHERE s.serviceId = :serviceId";

        TypedQuery<Service> query = entityManager.createQuery(jpql, Service.class);
        query.setParameter("serviceId", serviceId);

        List<Service> resultList = query.getResultList();
        return resultList.isEmpty() ? null : resultList.get(0);
    }

    @Override
    public List<Service> getRelatedServices(int serviceId) {
        String jpql = "SELECT DISTINCT s FROM Service s " +
                "LEFT JOIN FETCH s.serviceImages si " +
                "LEFT JOIN FETCH si.image i " +
                "WHERE s.serviceId <> :serviceId AND s.isActive = true";

        TypedQuery<Service> query = entityManager.createQuery(jpql, Service.class);
        query.setParameter("serviceId", serviceId);

        return query.getResultList();
    }

    @Override
    public List<Booking> findBookingsByUserId(int userId) {
        String jpql = "SELECT b FROM Booking b " +
                "JOIN FETCH b.service s " +
                "WHERE b.patient.userId = :userId " +
                "AND (b.status = 'Pending' OR b.status = 'Approved')";

        TypedQuery<Booking> query = entityManager.createQuery(jpql, Booking.class);
        query.setParameter("userId", userId);

        return query.getResultList();
    }

    @Override
    public List<Booking> findBookingsByPhoneNumber(String phoneNumber) {
        String jpql = "SELECT b FROM Booking b " +
                "JOIN FETCH b.service s " +
                "WHERE b.phoneNumber = :phoneNumber " +
                " AND (b.status = 'Pending' OR b.status = 'Approved')";

        TypedQuery<Booking> query = entityManager.createQuery(jpql, Booking.class);
        query.setParameter("phoneNumber", phoneNumber);

        return query.getResultList();
    }
}
