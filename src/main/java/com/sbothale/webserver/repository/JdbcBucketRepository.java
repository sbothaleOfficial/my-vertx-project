package com.sbothale.webserver.repository;

import com.sbothale.webserver.bucket.Bucket;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcBucketRepository implements BucketRepository {

    private final Connection connection;

    public JdbcBucketRepository(String url, String username, String password) throws SQLException {
        connection = DriverManager.getConnection(url, username, password);
    }

    @Override
    public List<Bucket> getAllBuckets() {
        List<Bucket> buckets = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM buckets");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Bucket bucket = new Bucket(resultSet.getString("id"), resultSet.getString("name"),
                        resultSet.getString("location"));
                buckets.add(bucket);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return buckets;
    }

    @Override
    public Optional<Bucket> getBucketById(String bucketId) {
        Bucket bucket = null;
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM buckets WHERE id=?")) {
            statement.setString(1, bucketId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    bucket = new Bucket(resultSet.getString("id"), resultSet.getString("name"),
                            resultSet.getString("location"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(bucket);
    }

    @Override
    public void createBucket(Bucket bucket) {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO buckets(id, name, location) VALUES(?,?,?)")) {
            statement.setString(1, bucket.getId());
            statement.setString(2, bucket.getName());
            statement.setString(3, bucket.getLocation());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteBucket(String bucketId) {
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM buckets WHERE id=?")) {
            statement.setString(1, bucketId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateBucket(Bucket bucket) {
        try (PreparedStatement statement = connection.prepareStatement("UPDATE buckets SET name=?, location=? WHERE id=?")) {
            statement.setString(1, bucket.getName());
            statement.setString(2, bucket.getLocation());
            statement.setString(3, bucket.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close() throws SQLException {
        connection.close();
    }
}
