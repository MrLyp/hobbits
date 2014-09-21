package me.hobbits.leimao.freevip.util;

import java.util.List;

import me.hobbits.leimao.freevip.db.ExchangeHandler;
import me.hobbits.leimao.freevip.db.IncomeHandler;
import me.hobbits.leimao.freevip.db.MessageHandler;
import me.hobbits.leimao.freevip.model.Balance;
import me.hobbits.leimao.freevip.model.BannerList;
import me.hobbits.leimao.freevip.model.BasicConfig;
import me.hobbits.leimao.freevip.model.Exchange;
import me.hobbits.leimao.freevip.model.ExchangeList;
import me.hobbits.leimao.freevip.model.Goods;
import me.hobbits.leimao.freevip.model.GoodsList;
import me.hobbits.leimao.freevip.model.Income;
import me.hobbits.leimao.freevip.model.IncomeList;
import me.hobbits.leimao.freevip.model.Message;
import me.hobbits.leimao.freevip.model.MessageList;
import me.hobbits.leimao.freevip.model.SignInSuccess;
import me.hobbits.leimao.freevip.model.TaskList;
import android.content.Context;
import cn.gandalf.util.PersistenceManager;

public class GlobalValue {

	private final String KEY_USER_INFO = "key_user_info";
	private final String KEY_BALANCE = "key_balance";
	private final String KEY_BANNER = "key_banner";
	private final String KEY_BASIC = "key_basic";
	private final String KEY_GOODS = "key_goods";
	private final String KEY_TASK = "key_task";

	private static GlobalValue mIns;
	private Context mContext;
	private SignInSuccess mUserInfo;
	private Balance mBalance;
	private BannerList mBannerList;
	private BasicConfig mBasicConfig;
	private GoodsList mGoodsList;
	private TaskList mTaskList;
	private List<Message> mMessages;
	private List<Exchange> mExchanges;
	private List<Income> mIncomes;
	private ExchangeHandler mExchangeHandler;
	private MessageHandler mMessageHandler;
	private IncomeHandler mIncomeHandler;

	public static GlobalValue getIns(Context context) {
		if (mIns == null) {
			mIns = new GlobalValue(context);
		}
		return mIns;
	}

	private GlobalValue(Context context) {
		mContext = context.getApplicationContext();
		mExchangeHandler = new ExchangeHandler(mContext);
		mMessageHandler = new MessageHandler(mContext);
		mIncomeHandler = new IncomeHandler(mContext);
	}

	public void updateUserInfo(SignInSuccess info) {
		mUserInfo = info;
		PersistenceManager.getInstance(mContext).putObject(KEY_USER_INFO,
				mUserInfo);
	}

	public SignInSuccess getUserInfo() {
		if (mUserInfo == null) {
			mUserInfo = (SignInSuccess) PersistenceManager
					.getInstance(mContext).getObject(KEY_USER_INFO);
		}
		return mUserInfo;
	}

	public void updateBalance(Balance balance) {
		mBalance = balance;
		PersistenceManager.getInstance(mContext).putObject(KEY_BALANCE,
				mBalance);
	}

	public Balance getBalance() {
		if (mBalance == null) {
			mBalance = (Balance) PersistenceManager.getInstance(mContext)
					.getObject(KEY_BALANCE);
		}
		return mBalance;
	}

	public void updateBanners(BannerList list) {
		mBannerList = list;
		PersistenceManager.getInstance(mContext).putObject(KEY_BANNER,
				mBannerList);
	}

	public BannerList getBanners() {
		if (mBannerList == null) {
			mBannerList = (BannerList) PersistenceManager.getInstance(mContext)
					.getObject(KEY_BANNER);
		}
		return mBannerList;
	}

	public void updateBasicConfig(BasicConfig config) {
		mBasicConfig = config;
		PersistenceManager.getInstance(mContext).putObject(KEY_BASIC,
				mBasicConfig);
	}

	public BasicConfig getBasicConfig() {
		if (mBasicConfig == null)
			mBasicConfig = (BasicConfig) PersistenceManager.getInstance(
					mContext).getObject(KEY_BASIC);
		return mBasicConfig;
	}

	public void updateGoodsList(GoodsList list) {
		mGoodsList = list;
		PersistenceManager.getInstance(mContext).putObject(KEY_GOODS,
				mGoodsList);
	}

	public GoodsList getGoodsList() {
		if (mGoodsList == null)
			mGoodsList = (GoodsList) PersistenceManager.getInstance(mContext)
					.getObject(KEY_GOODS);
		return mGoodsList;
	}

	public Goods getGoodsItem(int idx) {
		GoodsList list = getGoodsList();
		if (list == null || idx >= list.size())
			return null;
		return list.get(idx);
	}

	public void updateTasks(TaskList list) {
		mTaskList = list;
		PersistenceManager.getInstance(mContext).putObject(KEY_TASK, mTaskList);
	}

	public TaskList getTaskList() {
		if (mTaskList == null)
			mTaskList = (TaskList) PersistenceManager.getInstance(mContext)
					.getObject(KEY_TASK);
		return mTaskList;
	}

	public void updateMessages(MessageList list) {
		if (list == null || list.isEmpty())
			return;
		for (Message m : list) {
			mMessageHandler.insertMessage(m);
		}
		mMessages = mMessageHandler.queryAllMessage();
	}

	public List<Message> getMessageList() {
		if (mMessages == null)
			mMessages = mMessageHandler.queryAllMessage();
		return mMessages;
	}

	public void updateExchanges(ExchangeList list) {
		if (list == null || list.isEmpty())
			return;
		for (Exchange e : list) {
			mExchangeHandler.insertExchange(e);
		}
		mExchanges = mExchangeHandler.queryAllExchange();
	}

	public List<Exchange> getExchangeList() {
		if (mExchanges == null)
			mExchanges = mExchangeHandler.queryAllExchange();
		return mExchanges;
	}

	public void updateIncomes(IncomeList list) {
		if (list == null || list.isEmpty())
			return;
		for (Income i : list) {
			mIncomeHandler.insertIncome(i);
		}
		mIncomes = mIncomeHandler.queryAllIncome();
	}

	public List<Income> getIncomeList() {
		if (mIncomes == null)
			mIncomes = mIncomeHandler.queryAllIncome();
		return mIncomes;
	}

}
