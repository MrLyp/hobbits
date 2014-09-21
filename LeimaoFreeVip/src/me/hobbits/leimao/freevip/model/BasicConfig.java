package me.hobbits.leimao.freevip.model;

import cn.gandalf.json.JsonItem;

public class BasicConfig implements JsonItem {

	private static final long serialVersionUID = -282756808648668399L;
	private Version version;
	private int is_hidden;

	public Version getVersion() {
		return version;
	}

	public void setVersion(Version version) {
		this.version = version;
	}

	public int getIs_hidden() {
		return is_hidden;
	}

	public void setIs_hidden(int is_hidden) {
		this.is_hidden = is_hidden;
	}

	public static class Version {

		private String newest_version;
		private String download_url;
		private String release_note;
		private int need_update;

		public String getNewest_version() {
			return newest_version;
		}

		public void setNewest_version(String newest_version) {
			this.newest_version = newest_version;
		}

		public String getDownload_url() {
			return download_url;
		}

		public void setDownload_url(String download_url) {
			this.download_url = download_url;
		}

		public String getRelease_note() {
			return release_note;
		}

		public void setRelease_note(String release_note) {
			this.release_note = release_note;
		}

		public int getNeed_update() {
			return need_update;
		}

		public void setNeed_update(int need_update) {
			this.need_update = need_update;
		}

	}
}
