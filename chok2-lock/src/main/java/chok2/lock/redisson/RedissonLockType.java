package chok2.lock.redisson;

public enum RedissonLockType
{
	/** 可重入锁*/
	REENTRANT_LOCK,
	
	/** 公平锁*/
	FAIR_LOCK,
	
	/** 读锁*/
	READ_LOCK,
	
	/** 写锁*/
	WRITE_LOCK;
}
