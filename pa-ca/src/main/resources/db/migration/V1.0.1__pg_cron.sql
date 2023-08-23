-- Activate pg_cron
CREATE EXTENSION pg_cron;

-- Create Test table
CREATE TABLE test_table (
  id SERIAL PRIMARY KEY,
  time_of_creation TIMESTAMP DEFAULT NOW()
);

-- SP Test table
CREATE OR REPLACE FUNCTION insert_task_entry()
  RETURNS VOID AS $$
BEGIN
  INSERT INTO test_table DEFAULT VALUES;
END;
$$ LANGUAGE plpgsql;

-- Schedule Task to run every Minute
SELECT cron.schedule('process-updates', '*/1 * * * *', 'SELECT insert_task_entry()');